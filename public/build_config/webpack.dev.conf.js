var path = require('path');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
  entry: "./public/index.js",
  output: {
    path: "./public",
    filename: "bundle.js"
  },
  module: {
    loaders: [
      {
        test: /\.js$/, loaders: ['babel-loader'], exclude: /node_modules/
      },
      {
        test: /\.scss$/,
        loader: ExtractTextPlugin.extract('style-loader', 'css-loader?sourceMap!sass-loader?sourceMap')
      }
    ]
  },
  sassLoader: {
      includePaths: [path.resolve(__dirname, '../style')]
  },
  plugins: [
      new ExtractTextPlugin('main.css')
  ]
};
