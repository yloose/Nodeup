import React, { ReactNode, useEffect, useRef, useState} from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';
import { CSSTransition, TransitionGroup } from 'react-transition-group';
import NodeList from './NodeList';
import NodeInfo from './NodeInfo';
import './PageTransition.css';


const NodeView = function(props) {

	const location = useLocation();
						
	
	// TODO: Implement CSS Transisiton with routes *correctly*
	return (
		<TransitionGroup component={null}>
			<CSSTransition
				key={location.key}
				timeout={1000}
				classNames="nodePageTransition"
				mountOnEnter
				unmountOnExit
			>
				<Routes location={location} >
					<Route path="/" element={<NodeList />} />
					<Route path="/info" element={<NodeInfo />} />
				</Routes>
			</CSSTransition>
		</TransitionGroup>
	);
}

export default NodeView;