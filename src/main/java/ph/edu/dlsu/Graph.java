package ph.edu.dlsu;

import java.util.*;

public class Graph<E> {

    HashMap<E, LinkedList<E>> adjList = new HashMap<>();

    void addEdge(E src, E dest){
        if(!adjList.containsKey(src)){      //if no key yet make a key
            LinkedList<E> nodes= new LinkedList<>();
            nodes.add(dest);
            adjList.put(src,nodes);
        }
        else{       //if key already present just add to it
            LinkedList<E> nodes= adjList.get(src);
            nodes.add(dest);
            adjList.put(src,nodes);
        }
    }

    void deleteEdge(E src,E todel){
        if(adjList.containsKey(src)){      //if no key yet make a key
            LinkedList<E> nodes= adjList.get(src);
            nodes.remove(todel);
            adjList.put(src,nodes);
        }
    }

    Set getNodes(){
        return adjList.keySet();
    }
    void displayGraph(){
        for(Map.Entry m: adjList.entrySet()){
            System.out.println(m.getKey()+" ==> " + m.getValue());
        }
    }




    void bfsprint(E start){
        LinkedList<Character> holder1=(LinkedList<Character>) adjList.get(start).clone();
        holder1.addFirst((Character) start);
        Character[] vertices=holder1.toArray(new Character[adjList.size()]);
        int lastitem=lastitem(vertices);
        for(int i=0;i<vertices.length;i++){
            Character[] holder=adjList.get(vertices[i]).toArray(new Character[adjList.get(vertices[i]).size()]);
            for(int j=0;j<holder.length;j++){
                if(!contains(vertices,holder[j])){
                    vertices[lastitem]=holder[j];
                    lastitem++;
                }
            }
        }
        System.out.println(Arrays.toString(vertices));
    }

    void dfs(E start){
        Character[] vertices=new Character[adjList.size()];
        vertices[0]=(Character) start;
        Stack<Integer> paths=new Stack<>();
        Stack<Character> temp=new Stack<>();
        temp.push((Character) start);

        int pointer=0;
        int lastitem=1;

        while(temp.size()!=0){
            if(paths.size()==pointer){
                paths.push(0);
            }
            int val=paths.pop();
            Character curr=temp.pop();

            if(adjList.get(curr).size()<=val){
                break;
            }

            if((!contains(vertices,(Character) adjList.get(curr).get(val)))){ //if does not contain put in vertices
                vertices[lastitem]=(Character) adjList.get(curr).get(val);
                paths.push(val+1);
                temp.push(curr);
                temp.push(vertices[lastitem]);
                pointer++;
                lastitem++;

            }

            else if((pointer!=0)&&(vertices[pointer-1]==(Character) adjList.get(curr).get(val))){    //if next path is equal to past path
                Character[] holder1=adjList.get(curr).toArray(new Character[adjList.get(curr).size()]);
                int tester=holder1.length;
                for(int i=0;i<holder1.length;i++){
                    if(!contains(vertices,holder1[i])){
                        tester--;
                    }
                }
                if(tester<holder1.length){
                    E holder=adjList.get(curr).pop();
                    adjList.get(curr).addLast(holder);
                    temp.push(curr);
                    paths.push(val);

                }else{
                    pointer--;
                }

            }

            else{
                pointer--;
            }
        }
        System.out.println(Arrays.toString(vertices));
    }




    boolean contains(Character[] a, Character b){
        int val=0;
        for(int i=0;i<a.length;i++){
            if(a[i]==b){
                val++;
            }
        }
        return val!=0;
    }

    int lastitem(Character[] a){
        int i=0;
        while(a[i]!=null){
            i++;
        }
        return i;
    }

    void fillzero(int[] a){
        for(int i=0;i<a.length;i++){
            a[i]=0;
        }
    }
}
