import java.util.*;

// ==========================
// 1. BEHAVIORAL PATTERNS
// ==========================

// ---- Strategy Pattern ----
interface DeliveryFeeStrategy {
    double calculate(Order order);
}

class DistanceStrategy implements DeliveryFeeStrategy {
    public double calculate(Order order) {
        return order.distance * 2;
    }
}

class WeightStrategy implements DeliveryFeeStrategy {
    public double calculate(Order order) {
        return order.weight * 1.5;
    }
}

class PriorityStrategy implements DeliveryFeeStrategy {
    public double calculate(Order order) {
        return 50 + order.distance * 1.2;
    }
}

class Order {
    int distance;
    int weight;

    public Order(int distance, int weight) {
        this.distance = distance;
        this.weight = weight;
    }
}

class DeliveryApp {
    private DeliveryFeeStrategy strategy;

    public DeliveryApp(DeliveryFeeStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(DeliveryFeeStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculateFee(Order order) {
        return strategy.calculate(order);
    }
}

// ---- Observer Pattern ----
interface Investor {
    void update(double price);
}

class ConcreteInvestor implements Investor {
    private String name;

    public ConcreteInvestor(String name) {
        this.name = name;
    }

    public void update(double price) {
        System.out.println(name + " notified: Stock price updated to " + price);
    }
}

class Stock {
    private List<Investor> observers = new ArrayList<>();

    public void attach(Investor investor) {
        observers.add(investor);
    }

    public void detach(Investor investor) {
        observers.remove(investor);
    }

    public void notifyInvestors(double price) {
        for (Investor investor : observers) {
            investor.update(price);
        }
    }
}

// ==========================
// 2. CREATIONAL PATTERNS
// ==========================

// ---- Singleton Pattern ----
class Logger {
    private static Logger instance;
    private Logger() {}

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("Log entry: " + message);
    }
}

// ---- Factory Method Pattern ----
interface Shape {
    String draw();
}

class Circle implements Shape {
    public String draw() { return "Drawing Circle"; }
}

class Square implements Shape {
    public String draw() { return "Drawing Square"; }
}

class Triangle implements Shape {
    public String draw() { return "Drawing Triangle"; }
}

class ShapeFactory {
    public static Shape createShape(String type) {
        switch (type.toLowerCase()) {
            case "circle": return new Circle();
            case "square": return new Square();
            case "triangle": return new Triangle();
            default: throw new IllegalArgumentException("Unknown shape type");
        }
    }
}

// ==========================
// 3. STRUCTURAL PATTERNS
// ==========================

// ---- Adapter Pattern ----
interface PaymentProcessor {
    void pay(double amount);
}

class ThirdPartyGateway {
    public void makePayment(double total) {
        System.out.println("Processing payment of $" + total + " through ThirdPartyGateway");
    }
}

class PaymentAdapter implements PaymentProcessor {
    private ThirdPartyGateway gateway;

    public PaymentAdapter(ThirdPartyGateway gateway) {
        this.gateway = gateway;
    }

    public void pay(double amount) {
        gateway.makePayment(amount);
    }
}

// ---- Decorator Pattern ----
interface Coffee {
    double cost();
}

class SimpleCoffee implements Coffee {
    public double cost() { return 5; }
}

abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    public CoffeeDecorator(Coffee coffee) { this.coffee = coffee; }
}

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) { super(coffee); }
    public double cost() { return coffee.cost() + 2; }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) { super(coffee); }
    public double cost() { return coffee.cost() + 1; }
}

class CaramelDecorator extends CoffeeDecorator {
    public CaramelDecorator(Coffee coffee) { super(coffee); }
    public double cost() { return coffee.cost() + 3; }
}

// ==========================
// MAIN DEMO
// ==========================
public class DesignPatternDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Design Pattern Demo ---");
            System.out.println("1. Strategy Pattern (Delivery Fee)");
            System.out.println("2. Observer Pattern (Stock Notification)");
            System.out.println("3. Singleton Pattern (Logger)");
            System.out.println("4. Factory Method Pattern (Shape Factory)");
            System.out.println("5. Adapter Pattern (Payment Gateway)");
            System.out.println("6. Decorator Pattern (Coffee Shop)");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    Order order = new Order(10, 5);
                    DeliveryApp app = new DeliveryApp(new DistanceStrategy());
                    System.out.println("Distance-based fee: " + app.calculateFee(order));
                    app.setStrategy(new WeightStrategy());
                    System.out.println("Weight-based fee: " + app.calculateFee(order));
                    app.setStrategy(new PriorityStrategy());
                    System.out.println("Priority-based fee: " + app.calculateFee(order));
                    break;

                case 2:
                    Stock stock = new Stock();
                    Investor alice = new ConcreteInvestor("Alice");
                    Investor bob = new ConcreteInvestor("Bob");
                    stock.attach(alice);
                    stock.attach(bob);
                    stock.notifyInvestors(120.5);
                    stock.notifyInvestors(135.0);
                    break;

                case 3:
                    Logger logger1 = Logger.getInstance();
                    Logger logger2 = Logger.getInstance();
                    logger1.log("Application started.");
                    logger2.log("User logged in.");
                    System.out.println("Are both loggers same? " + (logger1 == logger2));
                    break;

                case 4:
                    Shape shape1 = ShapeFactory.createShape("circle");
                    System.out.println(shape1.draw());
                    Shape shape2 = ShapeFactory.createShape("triangle");
                    System.out.println(shape2.draw());
                    break;

                case 5:
                    ThirdPartyGateway gateway = new ThirdPartyGateway();
                    PaymentProcessor processor = new PaymentAdapter(gateway);
                    processor.pay(150);
                    break;

                case 6:
                    Coffee coffee = new SimpleCoffee();
                    System.out.println("Simple Coffee: " + coffee.cost());
                    coffee = new MilkDecorator(coffee);
                    coffee = new SugarDecorator(coffee);
                    System.out.println("Milk + Sugar Coffee: " + coffee.cost());
                    coffee = new CaramelDecorator(coffee);
                    System.out.println("Milk + Sugar + Caramel Coffee: " + coffee.cost());
                    break;

                case 0:
                    running = false;
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        sc.close();
    }
}
