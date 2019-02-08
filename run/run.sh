cd ../src
{
	java com.salifm.tetris.Main
} || {
	javac com/salifm/tetris/*
	java com.salifm.tetris.Main
}