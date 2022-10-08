import * as React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import NodeView from './views/nodes/NodeView';
import Sidebar from './components/sidebar/Sidebar';
import Topbar from './components/topbar/Topbar';
import { Navigate } from 'react-router-dom';
import './app.css';

class App extends React.Component<any> {
	constructor(props: any) {
		super(props);
	}

	componentDidMount() {
	}

	render() {
		return (
			<BrowserRouter>
				<Sidebar />
				<div className="h-full w-[calc(100%-20rem)] ml-80">
					<Routes location={this.props.location}>
						<Route path="/" element={<Navigate to="/nodes" />} />
						<Route path="/nodes/*" element={<NodeView />} />
					</Routes>
				</div>
			</BrowserRouter>
		)
	}
}

// TODO: Use React.StrictMode
ReactDOM.createRoot(document.getElementById("react") as HTMLElement).render(
	<App />
)