import React from 'react';


function requireAll(r) {
  r.keys().forEach(r);
}

requireAll(require.context('./icons/', true, /\.svg$/));

interface IconProps {
	name: String,
	className?: String,
	size?: number,
}

const Icon = function(props: IconProps) {
	
	return (
		<svg className={`icon ${props.className}`} fill="currentColor" stroke="currentColor" width={props.size} height={props.size}>
			<use xlinkHref={`#${props.name}`} />
		</svg>
	)
}

export default Icon;