#!/bin/bash

set -e

APP_COPYRIGHT="Copyright © $( date +%Y ) ${APP_DEVELOPER_NAME}"
APP_VERSION="${PROJECT_VERSION%%-*}"

main() {
	printLine
	printSettings
	printLine
	copyFiles
	detectModules
	createRuntimeImage
	if [[ "$OSTYPE" == "linux-gnu"* ]]
	then
		createPackageLinux
	elif [[ "$OSTYPE" == "darwin"* ]]
	then
		createPackageMac
	else
		echo "Unknown operating system: ${OSTYPE}"
		exit 1
	fi
	printLine
}

printLine() {
	echo "--------------------------------------------------"
}

printSettings() {
	echo "APP_CATEGORY: ${APP_CATEGORY}"
	echo "APP_COPYRIGHT: ${APP_COPYRIGHT}"
	echo "APP_DESCRIPTION: ${APP_DESCRIPTION}"
	echo "APP_DEVELOPER_MAIL: ${APP_DEVELOPER_MAIL}"
	echo "APP_DEVELOPER_NAME: ${APP_DEVELOPER_NAME}"
	echo "APP_ICON: ${APP_ICON}"
	echo "APP_JAR: ${APP_JAR}"
	echo "APP_MAIN: ${APP_MAIN}"
	echo "APP_NAME: ${APP_NAME}"
	echo "APP_VERSION: ${APP_VERSION}"
	echo "PROJECT_VERSION: ${PROJECT_VERSION}"
	echo "JAVA_HOME: ${JAVA_HOME}"
	echo "JAVA_VERSION: ${JAVA_VERSION}"
}

copyFiles() {
	cp -v "target/${APP_JAR}" "target/libs/"
}

detectModules() {
	echo "Detect required modules..."
	DETECTED_MODULES=$( "${JAVA_HOME}/bin/jdeps" \
		--class-path "target/libs/*" \
		--ignore-missing-deps \
		--print-module-deps \
		"target/libs/${APP_JAR}" )
	if [ $? -ne 0 ]
	then
		echo "$DETECTED_MODULES"
		exit 1
	fi
	echo "Detected modules: ${DETECTED_MODULES}"
}

createRuntimeImage() {
	echo "Creates runtime image..."
	"${JAVA_HOME}/bin/jlink" \
		--add-modules "${DETECTED_MODULES}" \
		--no-header-files \
		--no-man-pages \
		--output "target/package/runtime-image" \
		--strip-debug \
		--strip-native-commands
	echo "Done"
}

createPackageMac() {
	createPackageAppImage
	createPackageDMG
	createPackagePKG
}

createPackageLinux() {
	createPackageAppImage
	createPackageDEB
}

createPackageAppImage() {
	echo "Creates app image..."
	"${JAVA_HOME}/bin/jpackage" \
		--app-version "$APP_VERSION" \
		--copyright "$APP_COPYRIGHT" \
		--description "$APP_DESCRIPTION" \
		--dest "target/package" \
		--icon "$APP_ICON" \
		--input "target/libs" \
		--main-class "$APP_MAIN" \
		--main-jar "$APP_JAR" \
		--name "$APP_NAME" \
		--runtime-image "target/package/runtime-image" \
		--type "app-image" \
		--vendor "$APP_DEVELOPER_NAME"
	echo "Done"
}

createPackageDEB() {
	echo "Creates deb package..."
	"${JAVA_HOME}/bin/jpackage" \
		--app-version "$APP_VERSION" \
		--copyright "${APP_COPYRIGHT}" \
		--description "$APP_DESCRIPTION" \
		--dest "target/package" \
		--icon "$APP_ICON" \
		--input "target/libs" \
		--license-file "LICENSE" \
		--linux-deb-maintainer "$APP_DEVELOPER_MAIL" \
		--linux-menu-group "$APP_CATEGORY" \
		--linux-shortcut \
		--main-class "$APP_MAIN" \
		--main-jar "$APP_JAR" \
		--name "$APP_NAME" \
		--runtime-image "target/package/runtime-image" \
		--type "deb" \
		--vendor "$APP_DEVELOPER_NAME"
	echo "Done"
}

createPackageDMG() {
	echo "Creates dmg package..."
	"${JAVA_HOME}/bin/jpackage" \
		--app-version "$APP_VERSION" \
		--copyright "${APP_COPYRIGHT}" \
		--description "$APP_DESCRIPTION" \
		--dest "target/package" \
		--icon "$APP_ICON" \
		--input "target/libs" \
		--license-file "LICENSE" \
		--main-class "$APP_MAIN" \
		--main-jar "$APP_JAR" \
		--name "$APP_NAME" \
		--runtime-image "target/package/runtime-image" \
		--type "dmg" \
		--vendor "$APP_DEVELOPER_NAME"
	echo "Done"
}

createPackagePKG() {
	echo "Creates pkg package..."
	"${JAVA_HOME}/bin/jpackage" \
		--app-version "$APP_VERSION" \
		--copyright "${APP_COPYRIGHT}" \
		--description "$APP_DESCRIPTION" \
		--dest "target/package" \
		--icon "$APP_ICON" \
		--input "target/libs" \
		--license-file "LICENSE" \
		--main-class "$APP_MAIN" \
		--main-jar "$APP_JAR" \
		--name "$APP_NAME" \
		--runtime-image "target/package/runtime-image" \
		--type "pkg" \
		--vendor "$APP_DEVELOPER_NAME"
	echo "Done"
}

main
