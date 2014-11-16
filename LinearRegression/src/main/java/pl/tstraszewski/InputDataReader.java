package pl.tstraszewski;

import Jama.Matrix;
import javafx.util.Pair;
import jdk.nashorn.internal.objects.NativeArray;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomek on 2014-11-16.
 */
public class InputDataReader {

    public static Pair<Matrix,Matrix> readData(String path) throws Exception {

        File f = new File(path);

        if (f.exists()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

            String line;
            boolean first = true;
            int attributes = 0;
            List<String[]> ll= new ArrayList<String[]>();
            while((line = br.readLine()) != null){
                if(first){
                    first = false;
                    attributes = line.split(" ").length;
                    continue;
                }
                ll.add(line.split(" "));
            }

            Matrix X = new Matrix(ll.size(),attributes,0.0);
            Matrix Y = new Matrix(ll.size(),1,0.0);

            for(int i=0; i< ll.size(); i++){
                String[] arr = ll.get(i);
                for(int j=0; j< arr.length; j++){
                    if(j == arr.length - 1){
                        Y.set(i,0,Double.parseDouble(arr[j]));
                        //add ones
                        X.set(i,0,1.0);
                    }
                    else{
                        X.set(i,j+1,Double.parseDouble(arr[j]));
                    }

                }
            }

            return new Pair<Matrix,Matrix>(X,Y);

        } else {
            throw new Exception("File: " + path + " doesn't exists!");
        }

    }
}
