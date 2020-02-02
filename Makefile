.PHONY: build deploy
ENVIROMENTS := staging production

verify:
	@echo "TARGET is $(TARGET)"

ifndef TARGET
	$(error TARGET is undefined)
endif

ifeq ($(filter $(TARGET),$(ENVIROMENTS)),)
	$(error $(TARGET) does not exist in $(ENVIROMENTS))
endif


build: verify
	@docker build -t gcr.io/wed-$(TARGET)-analytics/embulk/embulk:latest .

deploy: verify
	@docker push gcr.io/wed-$(TARGET)-analytics/embulk/embulk:latest


