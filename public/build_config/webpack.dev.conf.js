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
        test: /\.jsx$/,
        loaders: ['babel-loader'],
        exclude: /node_modules/
      }
    ]
  }
};
