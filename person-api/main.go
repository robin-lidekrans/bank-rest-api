package main

import (
	"context"

	"net/http"

	"github.com/gin-gonic/gin"

	"github.com/segmentio/kafka-go"
)

const (
	topic = "bank.rest"
	kafkaBrokerAddress = "localhost:9092"
)

type person struct {
	Key  string `json:key`
	Name string `json:name`
}

var persons = []person{
	{Key: "1", Name: "Jakob Pogulis"},
	{Key: "2", Name: "Xena"},
	{Key: "3", Name: "Marcus Bendtsen"},
	{Key: "4", Name: "Zorro"},
	{Key: "5", Name: "Q"},
}

func writeKafka(msg string) {
	w := &kafka.Writer{
		Addr: kafka.TCP(kafkaBrokerAddress),
		Topic: topic,
		Async: true,
	}

	err := w.WriteMessages(context.TODO(),
			kafka.Message{
				Value: []byte(msg),
			},
	)

	if err != nil {
		print("failed to write messages:", err)
	}
	
	if err := w.Close(); err != nil {
		print("failed to close writer:", err)
	}
}

func listPersons(c *gin.Context) {
	writeKafka("Recieved listPersons request, reurning persons")
	c.JSON(http.StatusOK, persons)
}

func getPersonByKeyOrName(c *gin.Context) {
	writeKafka("Received getPerson query request")
	query := c.Param("query")
	for _, x := range persons {
		if (x.Key == query) || (x.Name == query) {
			writeKafka("Person query successful, returning bank")
			c.JSON(http.StatusOK, x)
			return
		}
	}
	writeKafka("No person found, returning nil")
	c.JSON(http.StatusOK, nil)
}

func main() {
	router := gin.Default()

	endpoint := "/person"

	router.GET(endpoint+"/list", listPersons)
	router.GET(endpoint+"/find.:query", getPersonByKeyOrName)
	router.NoRoute(func(c *gin.Context) {
		c.JSON(http.StatusOK, nil)
	})

	router.Run("localhost:8060")
}
