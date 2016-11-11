package ru.spbstu.ptime.interpreter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public interface ASTBuilder {
    DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    Program getProgram();
}