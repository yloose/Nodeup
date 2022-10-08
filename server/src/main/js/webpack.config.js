var path = require('path');

var root = path.join(__dirname, "../../../");

module.exports = (env) = {
	mode: 'development',
	entry: path.join(root, '/src/main/js/app.tsx'),
	output: {
		path: path.join(root, '/target/classes/static/built'),
		filename: 'bundle.js',
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
	cache: true,
};