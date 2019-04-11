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
            tokenAuthority: 'http://localhost:4444/auth/realms/vdc_access/protocol/openid-connect/token',
            token: '',
            kcLogin: false
        }
        this.getInfo = this.getInfo.bind(this);
        this.customLogin = this.customLogin.bind(this);
        this.userLoaded = this.userLoaded.bind(this);
        this.userUnLoaded = this.userUnLoaded.bind(this);
        this.handleToken = this.handleToken.bind(this);
        this.handleLogin = this.handleLogin.bind(this);

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
                    <div>{this.state.isAuthenticated ? <div/> :
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

        var formBody = [];
        for (var property in details) {
            var encodedKey = encodeURIComponent(property);
            var encodedValue = encodeURIComponent(details[property]);
            formBody.push(encodedKey + "=" + encodedValue);
        }

        const body = formBody.join("&");
        const options = {
            method: 'POST',
            uri: 'http://localhost:4444/auth/realms/vdc_access/protocol/openid-connect/token',
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
                let msg= JSON.parse(err.error).error_description
            this.setState({
                items: msg
            })
        })


    }

    withoutAuth=()=>{
        const rp = require('request-promise');
        const options={
            uri: 'http://localhost:8888/ask',
            resolveWithFullResponse: true

        }

        rp(options).then(response=> console.log(response)).catch(err => {
            console.log(err);
            let msg= err.error.message;
            this.setState({
                items: msg,
                customForm:false
            })
    })}

    handleToken(token) {

        this.setState({
            token: token,
            isAuthenticated: true,
            customForm: false
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

                uri: 'http://localhost:8888/ask',
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


}

ReactDOM.render(<App/>, document.getElementById("root"))
export default App;




