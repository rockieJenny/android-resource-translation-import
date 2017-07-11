package com.lionmobi;


public class Cell {
    private String content;
    private int cellNum;

    public Cell(int cellNum) {
        this.cellNum = cellNum;
    }

    public int getCellNum() {
        return cellNum;
    }

    public void setCellNum(int cellNum) {
        this.cellNum = cellNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Cell [content=" + content + ", cellNum=" + cellNum + "]";
    }

}
