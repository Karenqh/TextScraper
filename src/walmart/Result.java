package walmart;

public class Result {
	private String title = "";
	private String price = "";
	
	public Result(String title, String price) {
		this.title = title;
		this.price = price;
	}

	public void printInfo() {
		System.out.println("Product name: " + title);
		System.out.println("Product price: " + price + "\n");
	}
	
}
