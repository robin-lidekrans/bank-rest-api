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

type bank struct {
	Key  string `json:key`
	Name string `json:name`
}

var banks = []bank{
	{Key: "1", Name: "SWEDBANK"},
	{Key: "2", Name: "IKANOBANKEN"},
	{Key: "3", Name: "JPMORGAN"},
	{Key: "4", Name: "NORDEA"},
	{Key: "5", Name: "CITIBANK"},
	{Key: "6", Name: "HANDELSBANKEN"},
	{Key: "7", Name: "SBAB"},
	{Key: "8", Name: "HSBC"},
	{Key: "9", Name: "NORDNET"},
}

func writeKafka(msg string) {
	w := &kafka.Writer{
		Addr: kafka.TCP(kafkaBrokerAddress),
		Topic: topic,
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

func listBanks(c *gin.Context) {
	writeKafka("Received listBanks request, returning banks")
	c.JSON(http.StatusOK, banks)
}

func getBankByKeyOrName(c *gin.Context) {
	writeKafka("Received getBank query request")
	query := c.Param("query")
	for _, x := range banks {
		if (x.Key == query) || (x.Name == query) {
			writeKafka("Bank query successful, returning bank")
			c.JSON(http.StatusOK, x)
			return
		}
	}
	writeKafka("No bank found, returning nil")
	c.JSON(http.StatusOK, nil)
}

func main() {
	router := gin.Default()

	endpoint := "/bank"

	router.GET(endpoint+"/list", listBanks)
	router.GET(endpoint+"/find.:query", getBankByKeyOrName)
	router.NoRoute(func(c *gin.Context) {
		c.JSON(http.StatusOK, nil)
	})
	
	router.Run("localhost:8070")
}
