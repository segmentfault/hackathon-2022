package logger

import (
	"go.uber.org/zap"
	"os"
	"testing"
)

func TestInitLogger(t *testing.T) {
	InitLogger()

	zap.L().Info("logger info")
	zap.L().Error("logger error, It should be in both runtime.log and runtime_err.log")

	zap.S().Infow("suger info",
		"location", "runtime.log",
		"should be in runtime_err.log", "no")

	zap.S().Errorw("suger error",
		"location", "runtime.log and runtime_err.log",
		"should be in runtime_err.log", "yes")
}

func TestInitLoggerUnderDebugMode(t *testing.T) {
	InitLogger()

	zap.L().Debug("should not be here")

	err := os.Setenv("zap_level", "debug")
	if err != nil {
		t.Error("something unexpected")
	}

	InitLogger()

	zap.L().Debug("I'm here!")

}
