module.exports = {
  startLockTask: function (successCallback, errorCallback, adminClassName, whitelist, immersive) {
    if (adminClassName == null) {
      adminClassName = '';
    }
    if (!whitelist) {
    whitelist = [];
    }
    cordova.exec(successCallback, errorCallback, "LockTask", "startLockTask", [adminClassName, whitelist, immersive]);
  },
  stopLockTask: function (successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "LockTask", "stopLockTask", ['', []]);
  }
};
