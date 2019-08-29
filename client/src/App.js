import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import './App.css';
import {Jumbotron, Button} from 'reactstrap';
import Login from './Login';
import KCLogin from './KCLogin';


class App extends Component {

    constructor(props) {
        super(props);

        this.state = {
            items: '',
            form: <div>test</div>,
            customForm: false,
            user: undefined,
            isAuthenticated: false,
            tokenAuthority: 'https://keycloak.ditasbench.k8s.ise-apps.de/auth/realms/vdc_access/protocol/openid-connect/token',
            token: '',
            kcLogin: false,
            vdcUrl: 'https://vdc.ditasbench.k8s.ise-apps.de/ask',
            showForms:true
        }
        this.getInfo = this.getInfo.bind(this);
        this.customLogin = this.customLogin.bind(this);
        this.userLoaded = this.userLoaded.bind(this);
        this.userUnLoaded = this.userUnLoaded.bind(this);
        this.handleToken = this.handleToken.bind(this);
        this.handleLogin = this.handleLogin.bind(this);
        this.reset = this.reset.bind(this);

    }


    userLoaded(user) {
        if (user)
            this.setState({
                user: user,
                isAuthenticated: true
            });
    }

    userUnLoaded() {
        this.setState({
            user: undefined,
            isAuthenticated: false
        });
    }


    render() {
        return (

            <div className="App">
                <Jumbotron>
                    <h1 className="display-3">Ditas Example App</h1>
                    <p className="lead">This is a simple App to demonstrate the
                        Authentication Workflow in the DITAS project</p>
                    <hr className="my-2"/>
                    <div>{!this.state.showForms ?
                        <div>
                            <Button color="primary"
                                    onClick={this.reset}
                                    outline={true}> reset </Button>
                        </div>
                        :
                        <div>< p> You are not authenticated, please click here
                            to
                            authenticate.</p>
                            <KCLogin handleToken={this.handleToken}/>
                            <Button color="primary"
                                    onClick={this.customLogin}
                                    outline={true}> try custom
                                Login </Button>
                            <Button color="primary"
                                    onClick={this.withoutAuth}
                                    outline={true}> try without Auth</Button>

                        </div>
                    }</div>

                </Jumbotron>

                <div>{this.state.customForm ?
                    <Login handleLogin={this.handleLogin}
                           handleToken={this.handleToken}
                           tokenAuthority={this.state.tokenAuthority}
                           clientId='vdc_client'/> : ''}</div>
                <div>{this.state.items}</div>


            </div>


        );
    }


    handleLogin(event) {
        event.preventDefault();
        const details = {
            'client_id': 'vdc_client',
            'username': event.target.username.value,
            'grant_type': 'password',
            'password': event.target.password.value
        };

        const rp = require('request-promise');

        let formBody = [];
        for (let property in details) {
            let encodedKey = encodeURIComponent(property);
            let encodedValue = encodeURIComponent(details[property]);
            formBody.push(encodedKey + "=" + encodedValue);
        }

        const body = formBody.join("&");
        const options = {
            method: 'POST',
            uri: this.state.tokenAuthority,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: body,
            json: false,
            resolveWithFullResponse: true
        };

        rp(options)
            .then(response => {
                this.handleToken(JSON.parse(response.body).access_token)
            }).catch(err => {
            console.log(err);
            this.setState({
                items: err.error.message
            })
        })


    }

    withoutAuth = () => {
        const rp = require('request-promise');
        const options = {
            uri: this.state.vdcUrl,
            resolveWithFullResponse: true


        }

        rp(options).then(response => {
            console.log(response);
            this.setState({
                items: "your application is not secured by IAM",
                customForm: false,
                showForms:false
            })

        }).catch(err => {
            console.log(err);
            let msg = err.error.message;

            this.setState({
                items: msg,
                customForm: false,
                showForms: false
            })
        })
    }

    handleToken(token) {

        this.setState({
            token: token,
            isAuthenticated: true,
            customForm: false,
            showForms: false
        });

    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.state.token) {
            if (!this.state.items) {
                this.getInfo();
            }
        }
    }

    getInfo = () => {
        if (this.state.isAuthenticated) {
            const rp = require('request-promise');
            const options = {

                uri: this.state.vdcUrl,
                headers: {
                    'Authorization': 'bearer ' + this.state.token
                }
            }
            rp.get(options).catch(err => {
                console.log(err.toString())
            })
                .then(res => {
                    console.log("respone from VDC: " + res);
                    this.setState({
                        items: JSON.parse(res).mgs
                    })
                })

        }
        console.log(this.state.items)
    }


    customLogin() {
        this.setState({
            customForm: true
        })


    }

    reset() {
        this.setState({
            customForm: false,
            user: undefined,
            isAuthenticated: false,
            token: '',
            kcLogin: false,
            items: '',
            showForms:true
        })
    }


}

ReactDOM.render(<App/>, document.getElementById("root"))
export default App;




