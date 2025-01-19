import reactor.core.publisher.Mono 
import org.nasdanika.drawio.Node
import org.nasdanika.graph.processor.ProcessorElement

//drawio test-data/drawio-http/diagram.drawio http-server --http-port=8080 processor route
new java.util.function.BiFunction() {

	@ProcessorElement
	public Node element;
	
	def apply(request, response) {
		response.sendString(Mono.just(element.getLabel()))
  	}
	
}
