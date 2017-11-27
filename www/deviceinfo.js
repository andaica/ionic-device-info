var exec = require('cordova/exec');

exports.getDeviceInfo = function (successCallback, errorCallback) {
	cordova.exec(successCallback, errorCallback, 'DeviceInfoPlugin', 'getDeviceInfo', []);
};
