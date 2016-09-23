import React from 'react';
import {Route, IndexRoute} from 'react-router';
import ReactApp from './components/ReactApp';
import Home from './components/home/home';
import Recruitment from './components/recruitment/recruitment';


export default [
    <Route path='/' component={ReactApp}>
      <Route path='/join-the-team' component={Recruitment} />
      <IndexRoute component={Home} />
    </Route>
];
