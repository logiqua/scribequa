package com.javax0.lex;

public record Position (String fileName, int line, int column){
    public Position nextCharacter(){
        return new Position(fileName, line, column+1);
    }
    public Position nextLine()  {
        return new Position(fileName, line+1, 1);
    }

    @Override
    public String toString(){
        return fileName + ":" + line + ":" + column;
    }
}
