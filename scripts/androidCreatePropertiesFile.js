#!/usr/bin/env node

module.exports = function (context) {

    var fs = context.requireCordovaModule('fs'),
        path = context.requireCordovaModule('path');

    const PLUGIN_NAME = "cordova-plugin-finbox-risk-manager";
    var platformRoot = path.join(context.opts.projectRoot, 'platforms/android');
    var GRADLE_FILENAME = path.join(platformRoot, 'finbox.properties');
    // const GRADLE_FILENAME = path.resolve(process.cwd(), 'platforms', 'android', 'finbox.properties');

    fs.readFile(path.resolve(context.opts.projectRoot, 'package.json'), function (err, data) {
        var jsonData = JSON.parse(data)
        if (jsonData.cordova.platforms != "android") return
        var plugins = jsonData.cordova.plugins
        var requiredPlugin = plugins[PLUGIN_NAME]
        if (requiredPlugin != null) {
            var awsKey = requiredPlugin.AWS_KEY
            var awsSecret = requiredPlugin.AWS_SECRET
            var finboxRmVersion = requiredPlugin.FINBOX_RM_VERSION
            var finboxRmArtifact = requiredPlugin.FINBOX_RM_ARTIFACT
            var gradleTemplate = `AWS_KEY=${awsKey}
AWS_SECRET=${awsSecret}
FINBOX_RM_VERSION=${finboxRmVersion}
FINBOX_RM_ARTIFACT=${finboxRmArtifact}`
            setGradleTemplate(gradleTemplate)
        }
    });

    /**
    * Write finbox.properties with:
    */
    function setGradleTemplate(template) {
        fs.writeFile(GRADLE_FILENAME, template, 'utf8', function (err) {
            if (err) return console.log(PLUGIN_NAME, " FAILED TO WRITE ", GRADLE_FILENAME, " > ", template, err);
        });
    }
}