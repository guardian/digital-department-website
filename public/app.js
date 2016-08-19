import React from 'react';
import {render} from 'react-dom';
import {Router, browserHistory} from 'react-router';
import {setConfig} from './util/configStore';
import routes from './routes';

import './style/main.scss';



render(
    <Router
      routes={routes} />
    , document.getElementById('react-mount'));
