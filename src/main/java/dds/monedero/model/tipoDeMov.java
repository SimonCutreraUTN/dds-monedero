package dds.monedero.model;

public enum tipoDeMov {
  DEPOSITO {
    @Override
    public double factor(){
      return 1;
    }
  },
  EXTRACCION {
    @Override
    public double factor(){
      return -1;
    }
  };

  public abstract double factor();
}
