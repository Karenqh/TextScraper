package walmart;

public class Result {
	private String title = "";
	private float price = 0;
	
	public Result(String title, float price) {
		this.title = title;
		this.price = price;
	}

	public void printInfo() {
		System.out.println("Product name: " + title);
		System.out.println("Product price: $" + price);
	}
	
}
