// > java --source 21 --enable-preview  Main2.java

// Launch-Protokoll:
// 1. public vor protected
// 2. static vor non-static

/**
protected static void main() {
    System.out.println("protected static void main()");
}

protected void main(String[] args) {
    System.out.println("public void main()");
}
**/