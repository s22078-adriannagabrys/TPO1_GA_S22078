package zad1;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

public class MyFileVisitor implements FileVisitor<Path> {

    private final FileChannel outputFileChannel;
    public Charset inCharset = Charset.forName("Cp1250");
    public Charset outCharset = Charset.forName("UTF-8");

    public MyFileVisitor(Path path) throws IOException {
        this.outputFileChannel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        //reading from file
        FileChannel inputFileChannel = FileChannel.open(file, StandardOpenOption.READ);
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)inputFileChannel.size());
        while (inputFileChannel.read(byteBuffer) >= 0){
            byteBuffer.flip();
            CharBuffer charBuffer = inCharset.decode(byteBuffer);
            byteBuffer = outCharset.encode(charBuffer);
            //writing to new file
            outputFileChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        inputFileChannel.close();

        return FileVisitResult.CONTINUE;
    }

    public void close () throws IOException {
        outputFileChannel.close();
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }
}
