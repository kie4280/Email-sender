public class Core {

    public static String file = "", user = "", password = "", port = "587", smtp = "smtp.gmail.com";
    public int peoplenum = 0;
    private int startrow = 0;
    private int endrow = 0;
    private int emailcell = 0;
    private String[][] array;
    static boolean cancel = false;
    public static Indicator indicator;
    public static Gui gui;
    Excelreader excel;
    String header = null;

    public Core(Gui guiIn, Indicator indicatorIn) {

        gui = guiIn;
        indicator = indicatorIn;
        excel = new Excelreader(Core.file);
        array = excel.read();
        if (array == null) {
            return;
        }
        for (int a = 0; a <= excel.row; a++) {

            for (int b = 1; array[a][0] != null && b <= Integer.parseInt(array[a][0]); b++) {
                if (array[a][b] != null && array[a][b].equals("peoplestartshere")) {
                    startrow = a;
                    array[a][b] = null;
                    array[a][0] = Integer.toString(Integer.parseInt(array[a][0]) - 1);
                }
                if (array[a][b] != null && array[a][b].equals("peopleendshere")) {
                    endrow = a;
                    array[a][b] = null;
                    array[a][0] = Integer.toString(Integer.parseInt(array[a][0]) - 1);
                }
                if (array[a][b] != null && array[a][b].equals("email")) {
                    emailcell = b;
                    array[a][b] = null;
                }
            }
        }
        peoplenum = endrow - startrow + 1;
    }

    public void send() {

        if (array == null) {
            return;
        }
        People people;
        cancel = false;
        Emailsender sender = new Emailsender(smtp, port, user, password);
        indicator.update(0, peoplenum);
        int done = 1;
        for (int i = 0; i < peoplenum && !cancel; i++) {
            people = getData(i);
            if (people.to != null) {
                sender.send(people.to, people.subject, people.content);
            }
            indicator.update(done, peoplenum);
            done++;
        }
        if (!cancel) {
            indicator.label1.setText("done !");
            sender.close();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.exit(0);
        } else {
            gui.setEnterButtonEnable(true);
        }
    }

    public People getData(int number) {

        People people = new People();
        people.to = array[startrow + number][emailcell];
        array[startrow + number][emailcell] = null;
        people.subject = excel.subject;
        int maxcell = 0;
        for (int a = 0; a < startrow; a++) {

            for (int c = Integer.parseInt(array[a][0]); c > 0 && array[a][c] == null; c--) {
                array[a][0] = Integer.toString(c - 1);
            }
            maxcell = Math.max(maxcell, Integer.parseInt(array[a][0]));
        }
        for (int c = Integer.parseInt(array[startrow + number][0]); c > 0 && array[startrow + number][c] == null; c--) {
            array[startrow + number][0] = Integer.toString(c - 1);
        }
        maxcell = Math.max(maxcell, Integer.parseInt(array[startrow + number][0]));
        for (int a = 0; a < startrow; a++) {
            array[a][0] = Integer.toString(maxcell);
        }
        array[startrow + number][0] = Integer.toString(maxcell);
        if (header == null) {
            for (int a = 0; a < startrow; a++) {
                people.content += "<tr>\n";
                for (int b = 1; b <= Integer.parseInt(array[a][0]); b++) {
                    if (array[a][b] != null && array[a][b] != "skip") {
                        int colspan = 1;
                        int rowspan = 1;
                        while (array[a][b + colspan] != null && b + colspan <= Integer.parseInt(array[a][0]) && (array[a][b]).equals(array[a][b + colspan])) {
                            colspan++;
                        }
                        while (array[a + rowspan][b] != null && (array[a][b]).equals(array[a + rowspan][b])) {
                            array[a + rowspan][b] = "skip";
                            rowspan++;
                        }
                        people.content += "<td";
                        if (colspan != 1) {
                            people.content += " colspan=\"" + Integer.toString(colspan) + "\" align=\"center\"";
                        }
                        if (rowspan != 1) {
                            people.content += " rowspan=\"" + Integer.toString(rowspan) + "\" align=\"center\"";
                        }
                        people.content += ">" + array[a][b] + "</td>\n";
                        b += colspan - 1;

                    } else if (array[a][b] == null && array[a][b] != "skip") {
                        people.content += "<td></td>\n";
                    }
                }
                people.content += "</tr>\n";

            }
            header = people.content;
        }

        people.content = header + "<tr>\n";
        for (int a = 1; a <= Integer.parseInt(array[startrow + number][0]); a++) {
            if (array[startrow + number][a] != null) {
                people.content += "<td>" + array[startrow + number][a] + "</td>\n";
            } else {
                people.content += "<td></td>\n";
            }
        }
        people.content += "</tr>\n" + "</table>\n</BODY>";
        return people;
    }

}


