package ph.edu.dlsu;

public class BSTreeInt {

    public class Node{
        int item;
        String name;
        Node left = null;
        Node right = null;
        Node parent = null;
        public Node(int item,String name){
            this.item = item;
            this.name=name;
        }
        public Node(){

        }
    }
    Node root=null;
    Node curr=null;

    public void add(int val,String name){
        Node temp = new Node(val,name);
        if(root == null){
            root=temp;
        }else{
            curr=root;
            while(true){
                if(curr.item>val){
                    if(curr.left!=null){
                        curr=curr.left;
                    }else{
                        curr.left=temp;
                        curr.left.parent=curr;
                        break;
                    }
                }else if(curr.item<val){
                    if(curr.right!=null){
                        curr=curr.right;
                    }else{
                        curr.right=temp;
                        curr.right.parent=curr;
                        break;
                    }
                }else if(curr.item==val){
                    if(curr.left==null){
                        curr.left=temp;
                        curr.left.parent=curr;
                    }else{
                        curr=curr.left;
                        break;
                    }
                }
                else{
                    break;
                }
            }
        }

    }

    public void delete(int val){
        if(root == null){//break if empty
            return;
        }else if(root.item==val){// if root is to be deleted
            //get max from left
            curr=root;
            curr=curr.left;
            while(curr.right!=null){
                curr=curr.right;
            }
            root.item=curr.item;
            if(curr.parent.left.item==curr.item){
                curr.parent.left=null;
            }else{
                curr.parent.right=null;
            }
        }else{
            curr=root;
            while(true){
                if(curr==null){
                    break;
                }
                if(curr.item>val){
                    curr=curr.left;
                }else if(curr.item<val){
                    curr=curr.right;
                }else{
                    break;
                }
            }
            if(curr!=null){
                if(curr.left==null&&curr.right==null){ //check if leaf
                    //just delete
                    if((curr.parent.right!=null)&&(curr.parent.right.item==val)){
                        curr.parent.right=null;
                        curr=null;
                    }else if((curr.parent.left!=null)&&(curr.parent.left.item==val)) {
                        curr.parent.left=null;
                        curr=null;
                    }
                }else if(curr.left==null&&curr.right!=null){ //check if one child
                    curr.parent.left=curr.left;

                }else if(curr.left!=null&&curr.right==null){ //check if one child
                    curr.parent.right=curr.right;

                }else{//2 children
                    Node temp = curr;
                    temp=temp.left;
                    while(temp.right!=null){
                        temp=temp.right;
                    }
                    curr.item=temp.item;
                    if(temp.parent.left.item==temp.item){
                        temp.parent.left=null;
                    }else{
                        temp.parent.right=null;
                    }

                }
            }


        }

    }

    public void printpreorder(){
        preorder(root);
        System.out.println("\n");
    }

    public void preorder(Node a){
        if(a!=null){
            System.out.print(a.item+" ");
            if(a.left!=null){
                preorder(a.left);
            }
            if(a.right!=null){
                preorder(a.right);
            }
        }
    }

    public void printinorder(){
        inorder(root);
        System.out.println("\n");
    }

    public void inorder(Node a){
        if(a!=null){
            if(a.left!=null){
                inorder(a.left);
            }
            System.out.print(a.item+" ");
            if(a.right!=null){
                inorder(a.right);
            }
        }
    }

    public void printpostorder(){
        postorder(root);
        System.out.println("\n");
    }

    public void postorder(Node a){
        if(a!=null){
            if(a.left!=null){
                postorder(a.left);
            }
            if(a.right!=null){
                postorder(a.right);
            }
            System.out.print(a.item+" ");
        }
    }
}
