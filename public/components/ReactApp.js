import React from 'react';
import ReactDOM from 'react-dom';
import Header from '../components/header/header';
import Footer from '../components/footer/footer';

require('../stylesheets/main.scss');

export default class ReactApp extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <Header />
        <div>
          {this.props.children}
        </div>
        <Footer />
      </div>
    );
  }
}
