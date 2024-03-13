module.exports = {
  startLockTask: function (successCallback, errorCallback, adminClassName, whitelist) {
    if (adminClassName == null) {
      adminClassName = '';
    }
    if (!whitelist) {
    whitelist = [];
    }
    cordova.exec(successCallback, errorCallback, "LockTask", "startLockTask", [adminClassName, whitelist]);
  },
  stopLockTask: function (successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "LockTask", "stopLockTask", []);
  }
};
