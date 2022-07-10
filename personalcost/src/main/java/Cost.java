import java.sql.Date;

public class Cost {

    private String costItem;
    private double cost;
    private String costType;
    private Date currentDate;

    public Cost() {
    }

    public Cost(String costItem, double cost, String costType, Date currentDate) {
        this.costItem = costItem;
        this.cost = cost;
        this.costType = costType;
        this.currentDate = currentDate;
    }

    public String getCostItem() {
        return costItem;
    }

    public void setCostItem(String costItem) {
        this.costItem = costItem;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    @Override
    public String toString() {
        return "Cost{" +
                "costItem='" + costItem + '\'' +
                ", cost=" + cost +
                ", costType='" + costType + '\'' +
                ", currentDate=" + currentDate +
                '}';
    }
}
