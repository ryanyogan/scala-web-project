var webpack = require('webpack');
var path = require('path');

module.exports = {
  entry: './ui/entry.js',
  output: {
    path: path.resolve(__dirname + '/public/compiled'),
    filename: 'bundle.js',
  },
  module: {
    loaders: [
      {
        test: /\.jsx?$/,
        loader: 'babel-loader',
        include: /ui/,
        query: {
          presets: ['es2015', 'stage-0', 'react'],
        },
      },
    ],
  },
};