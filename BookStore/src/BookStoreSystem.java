import java.util.ArrayList;
import java.util.List;

abstract class Book{
	protected String isbn;
	protected String title;
	protected int pYear;
	protected double price;
	public Book(String isbn, String title, int pYear, double price) {
		this.isbn = isbn;
		this.title = title;
		this.pYear = pYear;
		this.price = price;
	}
	public String toString(){
		return "isbn: "+isbn+"\n"
		      +"title: "+title+"\n"
		      +"Year of Publication: "+pYear+"\n"
		      +"Price: "+price;
	}
	public String getIsbn() {
		return isbn;
	}
	public String getTitle() {
		return title;
	}
	public int getPYear() {
		return pYear;
	}
	public double getPrice() {
		return price;
	}
	public abstract boolean isForSale();
}
class PaperBook extends Book implements Shippable{
    private int quantity;
	public PaperBook(String isbn, String title, int pYear, double price,int quantity) {
		super(isbn, title, pYear, price);
		this.quantity=quantity;
	}
    
	public boolean available(int quantity) {
		return this.quantity>=quantity;
	}
	public void ship(int quantity,String address){
		System.out.println("an amount of "+quantity+" for the book:"+this.getTitle()+" is shipped to "+address);
	}
    public void reduceQuantity(int quantity){
    	this.quantity-=quantity;
    }
	public boolean isForSale() {
		return true;
	}
	
}

interface Shippable{
	void ship(int quantity,String address);
}
class EBook extends Book implements Email{
    private String fileType;
	public EBook(String isbn, String title, int pYear, double price,String fileType) {
		super(isbn, title, pYear, price);
		this.fileType=fileType;
	}
    
	
	public String getFileType() {
		return fileType;
	}


	public boolean isForSale() {
		return true;
	}


	
	public void sendByEmail(String emailAddress) {
		System.out.println("Book:"+this.getTitle()+" sent to:"+emailAddress);
		
	}
	
}
interface Email{
	void sendByEmail(String emailAddress);
}
class ShowcaseBook extends Book{

	public ShowcaseBook(String isbn, String title, int pYear) {
		super(isbn, title, pYear, 0);
		
	}


	public boolean isForSale() {
		
		return false;
	}
	
}
class BookStore{
	private ArrayList<Book> inventory;
	public BookStore(){
		this.inventory=new ArrayList<>();
	}
	public void addBook(Book b){
		inventory.add(b);
	}
	public void listItems(){
		for(Book b:inventory){
			System.out.println(b);
			System.out.println("---------------------");
		}
	}
	public ArrayList<Book> removeOutDated(int currYear){
		ArrayList<Book> res=new ArrayList<>();
		for(int i=inventory.size()-1;i>=0;i--){
			if(currYear-inventory.get(i).getPYear()>5){
				res.add(inventory.get(i));
				inventory.remove(i);
			}//assume books are outdated after 5 years
		}
		return res;
	}
	public void buyBook(String isbn,int quantity,String address,String email) throws Exception{
		boolean found=false;
		for(Book b:inventory){
			if(b.getIsbn().equals(isbn)){
				found=true;
				if(!b.isForSale())
					throw new Exception("this book is not for sale!");
				double totalPrice=b.getPrice()*quantity;
				if(b instanceof PaperBook){
					PaperBook pb=(PaperBook)b;
					if(!pb.available(quantity))
						throw new Exception("not enough stock!");
					pb.reduceQuantity(quantity);
					pb.ship(quantity, address);
					
				}
				else{
					if(b instanceof EBook){
						EBook eb=(EBook)b;
						eb.sendByEmail(email);
					}
				}
				System.out.println("book: "+b.getTitle()+" bought for: "+totalPrice);
				break;
			}
			
		}
		if(!found)
			throw new Exception("book not found!");
		
	}
}
