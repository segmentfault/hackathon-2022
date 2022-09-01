// Package logger
// zap.L() for standard logger
// eg.
//		zap.L().Debug()
// 		zap.L().Info()
// 		zap.L().Error()
// zap.S() for sugared logger with more useful and friendly features, but performance loss
// eg.
// 		zap.S().Debugf(...)
//		zap.S().Infoln(...)
//		zap.S().Errorw(...)
package logger

import (
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
	"os"
)

var logger *zap.Logger

const (
	ENV_LOG_LEVEL      = "zap_level"
	LOG_LOCATION       = "./runtime.log"
	ERROR_LOG_LOCATION = "./runtime_err.log"
)

func InitLogger() {
	encoder := getEncoder()

	// 默认INFO_LEVEL
	level := zapcore.InfoLevel

	l := os.Getenv(ENV_LOG_LEVEL)
	if l == "debug" {
		level = zapcore.DebugLevel
	}

	c1 := zapcore.NewCore(encoder, getLogWriter(LOG_LOCATION), level)

	c2 := zapcore.NewCore(encoder, getLogWriter(ERROR_LOG_LOCATION), zap.ErrorLevel)

	core := zapcore.NewTee(c1, c2)
	logger = zap.New(core, zap.AddCaller())

	zap.ReplaceGlobals(logger)
}

func getEncoder() zapcore.Encoder {
	encoderConfig := zap.NewProductionEncoderConfig()
	encoderConfig.EncodeTime = zapcore.ISO8601TimeEncoder
	encoderConfig.EncodeLevel = zapcore.CapitalLevelEncoder
	return zapcore.NewConsoleEncoder(encoderConfig)
}

func getLogWriter(location string) zapcore.WriteSyncer {
	file, err := os.OpenFile(location, os.O_RDWR|os.O_APPEND, 0664)
	if err != nil {
		panic(err)
	}
	return zapcore.AddSync(file)
}
