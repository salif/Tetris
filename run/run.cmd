cd ../src
%@Try%
	java com.salifm.tetris.Main
%@EndTry%
:@Catch
	javac com/salifm/tetris/*
	java com.salifm.tetris.Main
:@EndCatch