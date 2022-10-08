var path = require('path');
const webpack = require('webpack');

var root = path.join(__dirname, "../../../");

module.exports = {
	mode: 'development',
	entry: [
		'react-hot-loader/patch',
		// activate HMR for React

		'webpack-dev-server/client?http://localhost:8888',
		// bundle the client for webpack-dev-server
		// and connect to the provided endpoint

		'webpack/hot/only-dev-server',
		// bundle the client for hot reloading
		// only- means to only hot reload for successful updates

		// the entry point of our app
		path.join(root, '/src/main/js/app.tsx'),
	],
	output: {
		path: path.resolve(root, '/target/classes/static/built'),
		filename: 'bundle.js',
		publicPath: path.join(root, '/target/classes/static/built')
	},
	module: {
		rules: [
			{
				test: path.join(__dirname, '.'),
				exclude: /(node_modules)/,
				use: [{
					loader: 'babel-loader',
					options: {
						presets: ["@babel/preset-env", "@babel/preset-react", "@babel/preset-typescript"]
					}
				}]
			},
			{
				test: /\.css$/,
				use: [
					{ loader: "style-loader" },
					{ loader: "css-loader" },
					{ loader: "postcss-loader" }
				],
			},
			{
				test: /\.svg$/,
				loader: 'svg-sprite-loader',
			},
		]
	},
	resolve: {
		extensions: [".js", ".json", ".ts", ".tsx"],
	},
	devtool: "inline-source-map",
	devServer: {
		proxy: {
			"/": {
				target: {
					host: "localhost",
					protocol: 'http:',
					port: 8080,
				},
			},
			hot: true,
			ignorePath: true,
			changeOrigin: true,
			secure: false,
		},
		static: [
			{
				directory: path.resolve(root, "src/main/resources/static")
			}
		],
		port: 8888,
		host: "localhost",
	},
	cache: false,
};