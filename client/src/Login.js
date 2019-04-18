import React, {Component} from 'react';
import {Form, FormGroup, Button, Label, Input} from 'reactstrap';

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {isAuthenticated: false}

        this.handleLogin = this.handleLogin.bind(this)


    }


    render() {
        return (
            <Form onSubmit={this.handleLogin}>
                <FormGroup>
                    <Label for="Username">Username</Label>
                    <Input type="text" name="username" id="username"
                           placeholder="Username"/>
                </FormGroup>
                <FormGroup>
                    <Label for="password">Password</Label>
                    <Input type="password" name="password" id="password"
                           placeholder="Password"/>
                </FormGroup>
                <Button>Submit</Button>
            </Form>

        )
    }

    handleLogin(event) {    
        this.props.handleLogin(event);

    }


}

export default Login;