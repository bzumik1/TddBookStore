package bookstore;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class Progress {
    private final int completed;
    private final int toRead;
    private final int inProgress;
}
