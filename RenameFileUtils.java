import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RenameFileUtils {
    /**
     * @param pathDirectoryName 文件夹的路径
     * @param regex 匹配的正则表达式
     * @param replaceMent 替换的字符
     * @param startNumber 替换字符后面的数字
     * @param numberBits 数字的位数，如果为4，则0001
     * @return 重命名是否成功
     */
    public static boolean renameMulFileByIncrement(String pathDirectoryName,String regex,String replaceMent,int startNumber,int numberBits){
        File folderFile = new File(pathDirectoryName);
        if(!folderFile.exists()){
            System.out.println("file folder do not exist");
            return false;
        }
        if(!folderFile.isDirectory()){
            System.out.println(pathDirectoryName+" is not folder");
            return false;
        }
        if(!pathDirectoryName.endsWith("/")){
            pathDirectoryName +="/";
        }
        File[] files = folderFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(pathname.getName());
                return matcher.find();
            }
        });
        if(startNumber+files.length-1>Math.pow(10,numberBits)-1){
            System.out.printf("your numberBits(%d) is less than filesNumber(%d)\n",numberBits,files.length);
            return false;
        }
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (int i = 0; i < files.length; i++) {
            String replaceNumber = "";
            //补齐前面的零
            int bits = numberBits;
            int index = startNumber+i;
            while (bits>0){
                index/=10;
                bits--;
                if(index==0 && bits>0){
                    replaceNumber+="0";
                }
            }
            replaceNumber = replaceNumber+(i+1);
            String replace = replaceMent+replaceNumber;

            String filename = files[i].getName();
            String newFilename = filename.replaceAll(regex,replace);
            System.out.println(newFilename);
            boolean r = files[i].renameTo(new File(pathDirectoryName+newFilename));
            if(!r){
                System.out.printf("rename to file(%s) failed \n",pathDirectoryName+newFilename);
                return false;
            }
        }
        return true;
    }
}
