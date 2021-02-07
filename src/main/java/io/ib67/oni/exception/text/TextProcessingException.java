package io.ib67.oni.exception.text;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TextProcessingException extends Exception {
    public String reason;
}
