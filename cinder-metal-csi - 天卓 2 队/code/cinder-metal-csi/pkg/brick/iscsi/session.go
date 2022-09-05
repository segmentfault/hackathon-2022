package iscsi

import (
	"strconv"
	"strings"

	"github.com/kungze/cinder-metal-csi/pkg/brick/utils"
	"k8s.io/klog/v2"
)

type SessionIscsi struct {
	Transport            string
	SessionID            int
	TargetPortal         string
	TargetPortalGroupTag int
	IQN                  string
	NodeType             string
}

//GetSessions access to the iscsi sessions
func GetSessions() ([]SessionIscsi, error) {
	args := []string{"-m", "session"}
	out, err := utils.Execute("iscsiadm", args...)
	if err != nil {
		klog.Errorf("Exec iscsiadm -m session command failed", err)
		return nil, err
	}
	session, err := parseSession(out)
	if err != nil {
		klog.Errorf("Parse session info failed", err)
		return nil, err
	}
	return session, nil
}

//parseSession parse session content
func parseSession(out string) ([]SessionIscsi, error) {
	lines := strings.Split(strings.TrimSpace(out), "\n")
	re := strings.NewReplacer("[", "", "]", "")
	var session []SessionIscsi
	for _, line := range lines {
		l := strings.Fields(line)
		if len(l) < 4 {
			continue
		}
		protocol := strings.Split(l[0], ":")[0]
		id := re.Replace(l[1])
		id64, _ := strconv.ParseInt(id, 10, 32)
		portal := strings.Split(l[2], ",")[0]
		portalTag, err := strconv.Atoi(strings.Split(l[2], ",")[1])
		if err != nil {
			klog.Error("failed to parse portal port group tag", err)
			return nil, err
		}
		s := SessionIscsi{
			Transport:            protocol,
			SessionID:            int(id64),
			TargetPortal:         portal,
			TargetPortalGroupTag: portalTag,
			IQN:                  l[3],
			NodeType:             strings.Split(l[3], ":")[1],
		}
		session = append(session, s)

	}
	return session, nil
}
