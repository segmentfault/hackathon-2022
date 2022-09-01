package util

import "testing"

func TestSplitFileNameAndExt(t *testing.T) {
	fileName := "hello/hello/hello.txt"
	name, ext := SplitFileNameAndExt(fileName)
	if name != "hello/hello/hello" || ext != ".txt" {
		t.Error("Split FileName and Ext failed")
	}
}
