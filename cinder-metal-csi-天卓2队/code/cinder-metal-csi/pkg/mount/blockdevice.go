package mount

import (
	"fmt"
	"io"
	"os"

	"golang.org/x/sys/unix"
)

func IsBlockDevice(path string) (bool, error) {
	var st unix.Stat_t
	err := unix.Stat(path, &st)
	if err != nil {
		return false, fmt.Errorf("view the path %s stat info failed %v", path, err)
	}
	return (st.Mode & unix.S_IFMT) == unix.S_IFBLK, nil
}

func GetBlockDeviceSize(path string) (int64, error) {
	fd, err := os.Open(path)
	if err != nil {
		return 0, fmt.Errorf("open the file %s failed %v", path, err)
	}
	size, err := fd.Seek(0, io.SeekEnd)
	if err != nil {
		return 0, fmt.Errorf("error to seeking the file %s failed %v", path, err)
	}
	return size, nil
}
