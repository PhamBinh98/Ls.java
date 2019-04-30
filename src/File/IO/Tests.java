package File.IO;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class Tests {
    @Test
    public void ls() {
        List<String> Work = new ArrayList<>();
        Work.add("dir1 rwx 0");

        Work.add("dir2 rwx 0");

        Work.add("dir3 rwx 6084");

        Work.add("File rwx 244");

        Work.add("File1 rwx 0");

        Ls st = new Ls(new File("input"));
        assertEquals(Work, st.ls(false, false, false));



        Collections.reverse(Work);

        assertEquals(Work, st.ls(false, false, true));
        StringBuilder s = new StringBuilder();
        Work.clear();
        s.append("dir1 111").append(String.format("%12s", "0")).append(" 21.04.2019 20:39:13");
        Work.add(s.toString());
        Work.add("dir2 111" + String.format("%12s", "0") + " 21.04.2019 20:37:59");

        Work.add("dir3 111" + String.format("%12s", "6084") + " 21.04.2019 20:49:41");

        StringBuilder s3 = new StringBuilder();
        s3.append("File 111").append(String.format("%12s", "244")).append(" 21.04.2019 20:47:42");
        Work.add(s3.toString());

        Work.add("File1 111" + String.format("%12s", "0") + " 21.04.2019 20:40:08");
        st = new Ls(new File("input"));
        assertEquals(Work, st.ls(true, false, false));
        Work.clear();
        Work.add(s3.toString());
        st = new Ls(new File("input/File"));
        assertEquals(Work, st.ls(true, false, false));

        Work.clear();
        Work.add("dir1 rwx" + String.format("%8s", "0.0 b") + " 21.04.2019 20:39:13");

        Work.add("dir2 rwx" + String.format("%8s", "0.0 b") + " 21.04.2019 20:37:59");

        Work.add("dir3 rwx" + String.format("%8s", "5.9 Kb") + " 21.04.2019 20:49:41");

        Work.add("File rwx" + String.format("%8s", "244.0 b") + " 21.04.2019 20:47:42");

        Work.add("File1 rwx" + String.format("%8s", "0.0 b") + " 21.04.2019 20:40:08");

        st = new Ls(new File("input"));
        assertEquals(Work, st.ls(true, true, false));
        Collections.reverse(Work);
        assertEquals(Work, st.ls(true, true, true));
        Work.clear();
        Work.add("File1 rwx" + String.format("%8s", "0.0 b") + " 21.04.2019 20:40:08");
        st = new Ls(new File("input/File1"));
        assertEquals(Work, st.ls(true, true, false));

        Work.clear();
        Work.add("dir1 rwx 0");

        Work.add("dir2 rwx 0");

        Work.add("dir3 rwx 6084");

        Work.add("File rwx 244");

        Work.add("File1 rwx 0");

        st = new Ls(new File("input"));
        assertEquals(Work, st.ls(false, false, false));
        Work.clear();
        Work.add("File rwx 244.0 b");
        st = new Ls(new File("input\\File"));
        assertEquals(Work, st.ls(false, true, false));

    }

    @Test
    public void output() throws Exception {
        Ls st = new Ls(new File("input"));
        st.output("output.txt", st.ls(true, true, false));
        StringBuilder str = new StringBuilder();
        FileReader fr = new FileReader("output.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            str.append(line);
            str.append("\n");
        }
        assertEquals("dir1 rwx   0.0 b 21.04.2019 20:39:13\n" +
                "dir2 rwx   0.0 b 21.04.2019 20:37:59\n" +
                "dir3 rwx  5.9 Kb 21.04.2019 20:49:41\n" +
                "File rwx 244.0 b 21.04.2019 20:47:42\n" +
                "File1 rwx   0.0 b 21.04.2019 20:40:08\n", str.toString());
        Ls tb = new Ls(new File("input"));
        tb.output("output.txt", tb.ls(false, true, false));
        StringBuilder str1 = new StringBuilder();
        FileReader fr1 = new FileReader("output.txt");
        BufferedReader br1 = new BufferedReader(fr1);
        String line1;
        while ((line1 = br1.readLine()) != null) {
            str1.append(line1);
            str1.append("\n");
        }
        assertEquals("dir1 rwx 0.0 b\ndir2 rwx 0.0 b\ndir3 rwx 5.9 Kb\nFile rwx 244.0 b\nFile1 rwx 0.0 b\n", str1.toString());
        Ls ts = new Ls(new File("input"));
        ts.output("output.txt", ts.ls(true, false, true));
        StringBuilder str2 = new StringBuilder();
        FileReader fr2 = new FileReader("output.txt");
        BufferedReader br2 = new BufferedReader(fr2);
        String line2;
        while ((line2 = br2.readLine()) != null) {
            str2.append(line2);
            str2.append("\n");
        }
        assertEquals("File1 111           0 21.04.2019 20:40:08\n" +
                        "File 111         244 21.04.2019 20:47:42\n" +
                        "dir3 111        6084 21.04.2019 20:49:41\n" +
                        "dir2 111           0 21.04.2019 20:37:59\n" +
                        "dir1 111           0 21.04.2019 20:39:13\n"
                , str2.toString());

        Ls tn = new Ls(new File("input\\dir3"));
        tn.output("output.txt", tn.ls(true, true, false));
        StringBuilder str3 = new StringBuilder();
        FileReader fr3 = new FileReader("output.txt");
        BufferedReader br3 = new BufferedReader(fr3);
        String line3;
        while ((line3 = br3.readLine()) != null) {
            str3.append(line3);
            str3.append("\n");
        }
        assertEquals("bin rwx  5.9 Kb 21.04.2019 20:49:41\n"
                , str3.toString());
    }
}
