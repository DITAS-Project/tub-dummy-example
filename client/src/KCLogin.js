import React, {Component} from 'react';
import {Button} from 'reactstrap';
import Keycloak from 'keycloak-js';

class KCLogin extends Component {

    constructor(props) {
        super(props);
        this.state = {keycloak: null, authenticated: false};
        this.handleLogin = this.handleLogin.bind(this);
    }

    componentDidMount() {
        const keycloak = Keycloak('/keycloak.json');
        keycloak.init().then(authenticated => {
            this.setState({keycloak: keycloak, authenticated: authenticated});

        })

    }


    render() {
        if (this.state.keycloak) {
            if (this.state.authenticated) {
                return (<div>{this.state.keycloak.idToken}</div>);
            } else return (<Button color="primary"
                                   onClick={this.handleLogin}
                                   outline={true}>Keycloak
                Login</Button>)
        }
        return (
            <div>Initializing Keycloak...</div>
        );
    }

    handleLogin(event){
        event.preventDefault();
        this.state.keycloak.login();


    }
    componentDidUpdate(prevProps, prevState, snapshot) {
        if(this.state.keycloak){
            if(this.state.keycloak.idToken){
                this.props.handleToken(this.state.keycloak.token)
            }
        }
    }

}

export default KCLogin