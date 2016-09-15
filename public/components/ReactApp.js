import React, { Component } from 'react';
import ReactDOM from 'react-dom';

require('../stylesheets/main.scss');

class ReactApp extends Component {
  render() {
    return (
      <div className="ReactApp">
        <div className="ReactApp-header">
          <h2>The new Guardian page!</h2>
        </div>
      </div>
    );
  }
}

export default ReactApp;
