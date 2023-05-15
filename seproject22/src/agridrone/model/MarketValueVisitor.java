package agridrone.model;

public class MarketValueVisitor {
	public double doForItemContainer(ItemContainer item) {
        double sum = 0;
        if (item.getContents().size() > 0) {
            for (ItemAbstract current : item.getContents()) {
                if (current instanceof Item || current instanceof Drone) {
                    double mv = ((Item) current).getMarketValue();
                    sum += mv;
                } else {
                    double mv = ((ItemContainer) current).getContainerMV(new MarketValueVisitor());
                    sum += mv;
                }
            }

        }
        return sum;
    }
}
