runFDTests: BackendPlaceholder.java Frontend.java FrontendDeveloperTests.java
	javac BackendPlaceholder.java
	javac Frontend.java
	javac -cp ../junit5.jar:. FrontendDeveloperTests.java
	java -jar ../junit5.jar --class-path=. --select-class=FrontendDeveloperTests

runBDTests: BackendDeveloperTests.class
	java -jar ../junit5.jar -cp . -c BackendDeveloperTests

BackendDeveloperTests.class: DijkstraGraph.class BackendImplementation.class PlaceholderMap.class
	javac -cp .:../junit5.jar BackendDeveloperTests.java

BackendImplementation.class: PathObject.class BackendImplementation.java BackendInterface.java GraphADT.java
	javac BackendImplementation.java

PathObject.class: PathObject.java PathInterface.java
	javac PathObject.java

DijkstraGraph.class: DijkstraGraph.java BaseGraph.class GraphADT.java PlaceholderMap.class
	javac -cp .:../junit5.jar DijkstraGraph.java

PlaceholderMap.class: PlaceholderMap.java MapADT.java
	javac PlaceholderMap.java

BaseGraph.class: BaseGraph.java
	javac BaseGraph.java

clean:
	rm *.class
