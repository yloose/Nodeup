import config from '../../configuration/config';
import * as types from '../../types';

const datasinkConfig = config.datasinks.datasinkService;


export const datasinkService = {
	getAllAvailable,
};

function getAllAvailable() {
	return fetch(`${datasinkConfig.api.path}/getAllAvailable`, {
		method: "GET",
	}).then(res => {
		return res;
	}, error => {
		return error;
	})
}