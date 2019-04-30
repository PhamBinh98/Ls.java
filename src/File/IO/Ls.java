package File.IO;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
public class Ls {
    private List<File> Work;

    Ls(File file) {
        Work = new ArrayList<>();
        File[] list = file.listFiles();
        if (file.isDirectory()) {
            if (list == null) throw new AssertionError();
            Collections.addAll(this.Work, list);

        } else if (file.isFile()) this.Work.add(file);
        Collections.sort(Work);
    }

    private String isRWX(File file) {
        String res = "";
        if (file.canRead()) res += 'r';
        else res += '-';
        if (file.canWrite()) res += 'w';
        else res += '-';
        if (file.canExecute()) res += 'x';
        else res += '-';
        return res;
    }

    private String rwxToBit(File file) {
        String res = "";
        if (file.canRead()) res += '1';
        else res += '0';
        if (file.canWrite()) res += '1';
        else res += '0';
        if (file.canExecute()) res += '1';
        else res += '0';
        return res;
    }

    private String addHumanReadableLength(long s) {
        StringBuilder size = new StringBuilder();
        Formatter fmt = new Formatter(size);
        if (s > 1024 * 1024 * 1024) {
            fmt.format(Locale.ENGLISH, "%.1f Gb", (float) s / (1024 * 1024 * 1024));
        } else if (s > 1024 * 1024) {
            fmt.format(Locale.ENGLISH, "%.1f Mb", (float) s / (1024 * 1024 * 1024));
        } else if (s > 1024) {
            fmt.format(Locale.ENGLISH, "%.1f Kb", (float) s / 1024);
        } else fmt.format(Locale.ENGLISH, "%.1f b", (float) s);
        return size.toString();
    }

    private long size(File file) {
        long s = 0;
        if (file.isFile()) s = file.length();
        else {
            for (File b : file.listFiles()) {
                if (file.isFile()) s += b.length();
                else s += size(b);
            }
        }
        return s;
    }

    List<String> ls(boolean l, boolean h, boolean r) {
        List<String> res = new ArrayList<>();
        String pattern = "dd.MM.yyyy HH:mm:ss";
        SimpleDateFormat sim = new SimpleDateFormat(pattern);
        for (File i : Work) {
            StringBuilder s = new StringBuilder();
            Formatter fmt = new Formatter(s);
            s.append(i.getName());
            if (l && h) {
                fmt.format(" " + isRWX(i) + "%8s " + sim.format(i.lastModified()), addHumanReadableLength(size(i)));
            } else if (l) {
                fmt.format(" " + rwxToBit(i) + "%12s " + sim.format(i.lastModified()), size(i));

            } else if (h) fmt.format(" " + isRWX(i) + " " + addHumanReadableLength(size(i)));

            else fmt.format(" " + isRWX(i) + " " + size(i));

            res.add(s.toString());
        }

        if (r) Collections.reverse(res);

        return res;
    }


    void output(String o, List<String> list) throws IOException {
        PrintStream out;
        if (o == null)
            out = System.out;
        else {
            out = new PrintStream(new File(o));
        }
        for (String aList : list) {
            out.println(aList);
        }
    }

    public static void main(String[] args) throws Exception {
        Ls st = new Ls(new File(args[args.length - 1]));
        List<String> list = Arrays.asList(args);
        if (args[0].compareTo("ls") == 0) {
            boolean l = list.contains("-l");
            boolean h = list.contains("-h");
            boolean r = list.contains("-r");
            List<String> res = st.ls(l, h, r);
            String name;
            if (!list.contains("-o")) {
                name = null;
            } else {
                if (list.size() == list.indexOf("-o") + 3) {
                    name = list.get(list.indexOf("-o") + 1);
                }
                else name = "output";
            }
            st.output(name, res);
        } else {
            throw new AssertionError();
        }
    }
}
