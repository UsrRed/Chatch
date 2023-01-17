package test;

import java.util.ArrayList;

public class test_boucle {
    public static void main(String[] args){
        ArrayList<Object> annex = new ArrayList<>();
        annex.add("id_discussion");
        annex.add("1244 id de discu");
        annex.add("id_img");
        annex.add("1234 id de l'img");

        // partie a copier
        if(annex.size()%2==0){
            String txt = "";
            String txt2 = "";
            for(int i=0; i< annex.size()/2; i++){
                if(i==annex.size()/2-1){
                    txt += annex.get(i*2);
                    txt2 += annex.get(i*2+1);
                } else{
                    txt += annex.get(i*2) + ", ";
                    txt2 += annex.get(i*2+1) + ", ";
                }
            }
            System.out.println(txt + txt2);
        }
    }
}
