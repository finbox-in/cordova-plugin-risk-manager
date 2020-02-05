var exec = require('cordova/exec');

exports.createUser = function (apiKey, customerId, success, error) {
    exec(success, error, 'FinBoxRiskManager', 'createUser', [apiKey, customerId]);
};

exports.startPeriodicSync = function (duration, success, error) {
    exec(success, error, 'FinBoxRiskManager', 'startPeriodicSync', [duration]);
};

exports.stopPeriodicSync = function (success, error) {
    exec(success, error, 'FinBoxRiskManager', 'stopPeriodicSync', []);
};

exports.resetUserData = function (success, error) {
    exec(success, error, 'FinBoxRiskManager', 'resetUserData', []);
};
